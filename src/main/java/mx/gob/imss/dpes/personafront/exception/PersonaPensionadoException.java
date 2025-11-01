package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class PersonaPensionadoException extends BusinessException {

    public final static String ERROR_AL_ACTUALIZAR_DATOS_PENSIONADO = "msg15";

    public final static String ERROR_DESCONOCIDO_DEL_SERVIDOR = "msg17";

    public final static String ERROR_AL_VALIDAR_USUARIO = "msg21";

    public static final String ERROR_EN_RECUPERACION_TOKEN = "msg24";
    public static final String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg25";

    public PersonaPensionadoException(String messageKey) {
        super(messageKey);
    }

}
