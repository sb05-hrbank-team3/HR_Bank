package com.codeit.hrbank.dto.request;

public record BinaryContentCreateRequest(String fileName, String contentType, byte[] bytes) {

}