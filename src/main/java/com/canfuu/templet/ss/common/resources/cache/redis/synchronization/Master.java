package com.canfuu.templet.ss.common.resources.cache.redis.synchronization;

import com.canfuu.templet.ss.common.config.Constant;
import com.canfuu.templet.ss.common.entity.Node;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;
import com.canfuu.templet.ss.common.utils.ExceptionUtil;
import com.canfuu.templet.ss.common.utils.StringUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import redis.clients.jedis.Jedis;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 同步刷新类
 */
public class Master {
	private Set<Node<String,String>> ref = new HashSet<>();
	private String path = Master.class.getResource(Constant.MYBATIS_MAPPING_PATH).getPath();
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private	Jedis jedis = new Jedis("localhost",6379);

	public void excalibur() throws Exception {
		CanfuuLogger.log("连接主机",this,LogLevel.DEBUG);
		if(jedis!=null) {
			CanfuuLogger.log("连接成功", this, LogLevel.DEBUG);
		}else {
			CanfuuLogger.log("lose", this, LogLevel.DEBUG);
			throw new Exception(ExceptionUtil.explainError("Redis手动连接失败","请重新配置Redis手动连接"));
		}
		Iterator<Node<String,String>> i = ref.iterator();
		CanfuuLogger.log("ready to seihai",this,LogLevel.DEBUG);
		while (i.hasNext()){
			Node<String,String> node = i.next();
			Set<String> tables = new HashSet<>();
			String selectPrefix = "from ";
			tables.addAll(extractTableName(selectPrefix,node.value()));
			String updatePrefix = "update ";
			tables.addAll(extractTableName(updatePrefix,node.value()));
			String insertPrefix = "into ";
			tables.addAll(extractTableName(insertPrefix,node.value()));
			String deletePrefix = "delete ";
			tables.addAll(extractTableName(deletePrefix,node.value()));
			CanfuuLogger.log("tables:"+tables.toString(),this, LogLevel.DEBUG);
			CanfuuLogger.log("datakeys:"+node.value(),this,LogLevel.DEBUG);

			String datakey=node.key();
			Iterator<String> is = tables.iterator();

			while (is.hasNext()){
				String s = is.next();
				jedis.sadd(s,datakey);
			}
		}
		CanfuuLogger.log("Excalibur！！！",this,LogLevel.DEBUG);
	}


	private Set<String> extractTableName(String prefix,String statement){
		Set<String> set = new HashSet<>();
		if(statement.contains(prefix)) {
			String s = statement.substring(statement.indexOf(prefix) + prefix.length());
			set.addAll(extractTableName(prefix,s));
			int i =-1;
			char[] character = s.toCharArray();
			for (int j = 0; j < character.length; j++) {
				char c = character[j];
				if(!isName(c)){
					i = j;
					break;
				}
				if(j==character.length-1){
					i=j+1;
				}
			}
			if(i==-1){
				return set;
			}
			set.add(s.substring(0,i));
			return set;
		}else {
			return set;
		}
	}
	private boolean isName(Character c){
		return Character.isUpperCase(c) || Character.isLowerCase(c) || Character.isDigit(c) || c.equals('_');
	}

	@SuppressWarnings("unchecked")
	@Scheduled(cron="0 0 0/12 * * ? ")
	public void init() {
		CanfuuLogger.log("Master检测开始",this,LogLevel.DEBUG);
		CanfuuLogger.log("准备扫描周围环境 ->"+path,this,LogLevel.DEBUG);
		try {
			for (File temp : new File(path).listFiles((dir, name) -> name.contains("xml"))) {
				CanfuuLogger.log("扫描资源:"+temp.getName(),this,LogLevel.DEBUG);
				Document document = dbf.newDocumentBuilder().parse(path + "/" + temp.getName());
				String daoName = StringUtil.upCaptureName(temp.getName().replace(".xml",""));

				CanfuuLogger.log("补魔step.1 select scanning",this,LogLevel.DEBUG);
				NodeList selectList = document.getElementsByTagName("select");
				ref.addAll(supplementalMana(selectList,daoName));

				CanfuuLogger.log("补魔step.2 update scanning",this,LogLevel.DEBUG);
				NodeList updateList = document.getElementsByTagName("update");
				ref.addAll(supplementalMana(updateList,daoName));

				CanfuuLogger.log("补魔step.3 delete scanning",this,LogLevel.DEBUG);
				NodeList deleteList = document.getElementsByTagName("delete");
				ref.addAll(supplementalMana(deleteList,daoName));

				CanfuuLogger.log("补魔step.4 insert scanning",this,LogLevel.DEBUG);
				NodeList insertList = document.getElementsByTagName("insert");
				ref.addAll(supplementalMana(insertList,daoName));

				CanfuuLogger.log("finish a round to supplemental mana",this,LogLevel.DEBUG);
			}
			CanfuuLogger.log("Excalibur x 准备完成",this,LogLevel.DEBUG);
			excalibur();
		} catch (Exception e) {
			CanfuuLogger.log(e,this,LogLevel.ERROR);
		}
	}
	private Set<Node<String,String>> supplementalMana(NodeList statement, String daoName){
		Set<Node<String,String>> set = new HashSet<>();
		for (int i = 0; i < statement.getLength(); i++) {
			try{
				org.w3c.dom.Node node=statement.item(i);
				if(node.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE){
					String key = node.getAttributes().getNamedItem("id").getNodeValue();
					jedis.hset("datakey:dao",key,daoName);
					String value  = node.getFirstChild().getNodeValue().replace("\n" ,"").toLowerCase();
					set.add(new Node<>(key,value));
				}
			} catch(Exception e){
				CanfuuLogger.log(e,this,LogLevel.ERROR);
			}
		}
		return set;
	}
}
