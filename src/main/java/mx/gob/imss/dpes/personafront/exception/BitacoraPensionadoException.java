package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class BitacoraPensionadoException extends BusinessException {

    public final static String ERROR_AL_GUARDAR_BITACORA_DEL_PENSIONADO = "msg18";

    public final static String ERROR_DESCONOCIDO_DEL_SERVIDOR = "msg17";

    public BitacoraPensionadoException(String messageKey) {
        super(messageKey);
    }

}
