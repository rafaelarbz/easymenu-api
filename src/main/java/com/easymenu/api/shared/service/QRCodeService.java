package com.easymenu.api.shared.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodeService {
    byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException;
}
