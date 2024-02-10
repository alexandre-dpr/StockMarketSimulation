package com.example.community.dto.request;

public record ConsulDTO(String LockIndex, String Key, Integer Flags, String Value, Integer CreateIndex, Integer ModifyIndex) {
}
