package my.consler.catthebuilder.build;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;
import com.android.apksig.ApkSigner;
import com.android.apksig.ApkSigner.SignerConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Signer
{
    public static void sign(File inputApk, File outputApk, File keystoreFile, String keystorePassword, String keyAlias, String keyPassword, Context context)
    {
        try
        {
            Security.removeProvider("BC");
            Security.insertProviderAt(new BouncyCastleProvider(), 1);

            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (FileInputStream in = new FileInputStream(keystoreFile))
            {
                ks.load(in, keystorePassword.toCharArray());
            }
            PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
            Certificate[] certChain = ks.getCertificateChain(keyAlias);
            List<X509Certificate> x509Certs = Arrays.stream(certChain).map(cert -> (X509Certificate) cert).collect(Collectors.toList());

            SignerConfig signerConfig = new SignerConfig.Builder(keyAlias, privateKey, x509Certs).build();

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
        catch (Exception e)
        {
            Toast.makeText(context, "Something went wrong while signing the APK", Toast.LENGTH_SHORT).show();
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", e.getMessage());
            clipboard.setPrimaryClip(clip);
            throw new RuntimeException(e);
        }



    }
}
