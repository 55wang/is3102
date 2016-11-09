/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception.dams;

/**
 *
 * @author leiyang
 */
public class UpdateDepositAccountException extends Exception {

    /**
     * Creates a new instance of <code>CustomerNotExistException</code> without
     * detail message.
     */
    public UpdateDepositAccountException() {
    }

    /**
     * Constructs an instance of <code>CustomerNotExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDepositAccountException(String msg) {
        super(msg);
    }
}