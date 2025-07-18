package my.consler.catthebuilder.utils;

import com.android.apksig.ApkSigner;
import com.android.apksig.ApkSigner.SignerConfig;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SignApk
{
    public static void sign(File inputApk,
                            File outputApk,
                            File keystoreFile,
                            String keystorePassword,
                            String keyAlias,
                            String keyPassword) throws Exception {

        // Load keystore
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream in = new FileInputStream(keystoreFile)) {
            ks.load(in, keystorePassword.toCharArray());
        }
        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
        Certificate[] certChain = ks.getCertificateChain(keyAlias);

        // Convert Certificate[] to List<X509Certificate>
        List<X509Certificate> x509Certs = Arrays.stream(certChain)
                .map(cert -> (X509Certificate) cert)
                .collect(Collectors.toList());

        // Create signer config
        SignerConfig signerConfig = new SignerConfig.Builder(
                keyAlias, privateKey, x509Certs
        ).build();

        // Use no-argument Builder, then set all parameters
        ApkSigner.Builder builder = new ApkSigner.Builder(List.of(signerConfig));
        builder.setInputApk(inputApk);
        builder.setOutputApk(outputApk);
        builder.setV1SigningEnabled(true);
        builder.setV2SigningEnabled(true);
        builder.setV3SigningEnabled(true);
        builder.setV4SigningEnabled(false);

        // Sign
        ApkSigner signer = builder.build();
        signer.sign();
    }
}
