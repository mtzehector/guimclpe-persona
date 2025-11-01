/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author juan.garfias
 */
public class PersonasRQException extends BusinessException {

    public final static String KEY = "msg13";

    public PersonasRQException() {
        super(KEY);
    }

    public PersonasRQException(String messageKey) {
        super(messageKey);
    }

}
