package com.example.server.controller;

import com.example.server.dto.QRCodeRequest;
import com.example.server.service.QRCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody QRCodeRequest request) {
        String key = qrCodeService.generateQRCodeKey(
                request.getCourseId(),
                request.getStartTime(),
                request.getEndTime()
        );

        String qrCodeUrl = "https://example.com/scan?key=" + key; // Embed in QR code

        return ResponseEntity.ok(qrCodeUrl);
    }
}

