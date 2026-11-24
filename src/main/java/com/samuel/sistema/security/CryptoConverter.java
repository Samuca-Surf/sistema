package com.samuel.sistema.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Criptografia AES-256-GCM transparente para campos sensíveis (CPF, CNPJ).
 * Formato salvo no banco: Base64( IV(12 bytes) + texto cifrado + tag ).
 * A chave vem de configuração (variável de ambiente CRYPTO_KEY), nunca do código.
 */
@Converter
@Component
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int TAMANHO_IV = 12;
    private static final int TAMANHO_TAG_BITS = 128;

    private final SecretKeySpec chave;
    private final SecureRandom random = new SecureRandom();

    public CryptoConverter(@Value("${app.crypto.key}") String chaveBase64) {
        this.chave = new SecretKeySpec(Base64.getDecoder().decode(chaveBase64), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String valor) {
        if (valor == null) return null;
        try {
            byte[] iv = new byte[TAMANHO_IV];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, new GCMParameterSpec(TAMANHO_TAG_BITS, iv));
            byte[] cifrado = cipher.doFinal(valor.getBytes(StandardCharsets.UTF_8));

            byte[] saida = new byte[iv.length + cifrado.length];
            System.arraycopy(iv, 0, saida, 0, iv.length);
            System.arraycopy(cifrado, 0, saida, iv.length, cifrado.length);
            return Base64.getEncoder().encodeToString(saida);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao criptografar dado sensível", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String valor) {
        if (valor == null) return null;
        try {
            byte[] entrada = Base64.getDecoder().decode(valor);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, new GCMParameterSpec(TAMANHO_TAG_BITS, entrada, 0, TAMANHO_IV));
            byte[] aberto = cipher.doFinal(entrada, TAMANHO_IV, entrada.length - TAMANHO_IV);
            return new String(aberto, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao descriptografar dado sensível", e);
        }
    }
}
