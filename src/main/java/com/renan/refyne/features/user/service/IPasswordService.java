package com.renan.refyne.features.user.service;

public interface IPasswordService {

  String hash(String password);

  boolean matches(String raw, String encoded);
}
