package com.canfuu.templet.ss.common.utils.factory;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: ValidatorFactory
 */
public final class ValidatorFactory {

    private static Validator validator;

    public static Validator getValidator(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        return validator;
    }
}
