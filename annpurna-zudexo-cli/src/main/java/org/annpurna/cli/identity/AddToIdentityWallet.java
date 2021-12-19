/*
SPDX-License-Identifier: Apache-2.0
*/

package org.annpurna.cli.identity;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.annpurna.cli.common.utils.CommonUtils;
import org.annpurna.cli.common.utils.ResourceAdapter;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class AddToIdentityWallet {

  private static ResourceAdapter props = ResourceAdapter.getInstance("annpurna-id-wallet.properties");
		  
		  
  private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
    try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
      return Identities.readX509Certificate(certificateReader);
    }
  }

  private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
    try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
      return Identities.readPrivateKey(privateKeyReader);
    }
  }

  public static void main(String[] args) {
    try {
      // A wallet stores a collection of identities
      String orgMspID = props.getProperty("org.msp.id");
      
      Path walletPath = Paths.get(props.getProperty("wallet.path"));
      Wallet wallet = Wallets.newFileSystemWallet(walletPath);

      Path credentialPath = Paths.get(props.getProperty("org.user.msp.path"));
      System.out.println("credentialPath: " + credentialPath.toString());
      Path certificatePath = credentialPath.resolve(Paths.get(props.getProperty("org.user.cert.path")));
      System.out.println("certificatePem: " + certificatePath.toString());

      String privateKeyFileName = CommonUtils.resolvePrivateKeyPath(props.getProperty("org.user.private.key.path"));
      System.out.println("Private Key File :"+privateKeyFileName);
      Path privateKeyPath = credentialPath.resolve(Paths.get(privateKeyFileName));

      X509Certificate certificate = readX509Certificate(certificatePath);
      PrivateKey privateKey = getPrivateKey(privateKeyPath);

      Identity identity = Identities.newX509Identity(orgMspID, certificate, privateKey);


      String identityLabel = props.getProperty("org.user.id");
      wallet.put(identityLabel, identity);

      System.out.println("Write wallet info into " + walletPath.toString() + " successfully.");

    } catch (IOException | CertificateException | InvalidKeyException e) {
      System.err.println("Error adding to wallet");
      e.printStackTrace();
    }
  }

}
