package com.hedera.hashgraph.corda_hcs.notary;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.proto.Key;
import com.hedera.hashgraph.proto.SignaturePair;
import com.hedera.hashgraph.sdk.crypto.PublicKey;

import org.bouncycastle.math.ec.rfc8032.Ed25519;

// fixme: we can't use the `Ed25519PublicKey` type in the Hedera SDK
// because that uses `Ed25519PublicKeyParameters` which don't appear in the version of
// BouncyCastle (1.60) that Corda *insists* on using (dependency on a newer version is overridden).
class Ed25519PublicKey extends PublicKey {
    private final byte[] publicKeyBytes;

    Ed25519PublicKey(byte[] publicKeyBytes) {
        this.publicKeyBytes = publicKeyBytes;
    }

    static Ed25519PublicKey fromPrivateKey(byte[] privateKey) {
        byte[] publicKeyBytes = new byte[32];
        Ed25519.generatePublicKey(privateKey, 0, publicKeyBytes, 0);
        return new Ed25519PublicKey(publicKeyBytes);
    }

    @Override
    public Key toKeyProto() {
        return Key.newBuilder().setEd25519(ByteString.copyFrom(publicKeyBytes)).build();
    }

    @Override
    public byte[] toBytes() {
        return publicKeyBytes;
    }

    @Override
    public SignaturePair.SignatureCase getSignatureCase() {
        return SignaturePair.SignatureCase.ED25519;
    }
}
