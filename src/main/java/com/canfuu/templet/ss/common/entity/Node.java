package com.canfuu.templet.ss.common.entity;

import java.io.Serializable;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Node节点数据结构
 */
public class Node<K,V> implements Serializable{
	K k;
	V v;
	public Node(K k,V v){
		this.k=k;
		this.v=v;
	}
	public K key(){
		return k;
	}
	public V value(){
		return v;
	}

	@Override
	public String toString() {
		return "K=" + k + ", V=" + v + '\n';
	}
}
