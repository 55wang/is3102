/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author leiyang
 */
// Solve class file for org.atmosphere.config.managed.Decoder not found
public interface Decoder<U, T> {
    T decode(U s);
}
