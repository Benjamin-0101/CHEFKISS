package com.chefkiss.app

import com.chefkiss.app.utils.Validator
import org.junit.Test
import org.junit.Assert.*

class ValidatorTest {

    @Test
    fun `email valido retorna true`() {
        val email = "usuario@test.com"
        val resultado = Validator.isEmailValid(email)
        assertTrue(resultado)
    }

    @Test
    fun `email invalido retorna false`() {
        val email = "correo_sin_arroba"
        val resultado = Validator.isEmailValid(email)
        assertFalse(resultado)
    }

    @Test
    fun `password corto retorna false`() {
        val pass = "123"
        val resultado = Validator.isPasswordValid(pass)
        assertFalse(resultado)
    }

    @Test
    fun `password correcto retorna true`() {
        val pass = "123456"
        val resultado = Validator.isPasswordValid(pass)
        assertTrue(resultado)
    }
}