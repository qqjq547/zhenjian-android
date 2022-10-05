package com.watayouxiang.imclient.packet;

import androidx.annotation.NonNull;

import com.watayouxiang.imclient.model.body.HandshakeReq;

public class TioHandshake {
    // 客户端通过http登录后，服务器返回给客户端的token值
    private String token;
    // 握手请求密钥
    private final String handshakeKey;
    // 渠道
    private final String cid;
    // 命令码
    private final short command;
    // 极光推送 registerId
    private final String jpushinfo;

    private TioHandshake(String token,
                         String handshakeKey,
                         String cid,
                         short command,
                         String jpushinfo) {
        this.token = token;
        this.handshakeKey = handshakeKey;
        this.cid = cid;
        this.command = command;
        this.jpushinfo = jpushinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }

    public String getHandshakeKey() {
        return handshakeKey;
    }

    public String getCid() {
        return cid;
    }

    public short getCommand() {
        return command;
    }

    public TioPacket getPacket() {
        HandshakeReq handshakeReq = TioBodyBuilder.getHandshakeReq(cid, token, handshakeKey, jpushinfo);
        return TioPacketBuilder.getPacket(handshakeReq, command);
    }

    @Override
    public String toString() {
        return "TioHandshake{" +
                "token='" + token + '\'' +
                ", handshakeKey='" + handshakeKey + '\'' +
                ", cid='" + cid + '\'' +
                ", command=" + command +
                ", jpushinfo='" + jpushinfo + '\'' +
                '}';
    }

    public static class Builder {
        private final String token;
        private final String handshakeKey;
        private final short command;
        private String cid;
        private String jpushinfo;

        public Builder(String token, String handshakeKey, short command) {
            this.token = token;
            this.handshakeKey = handshakeKey;
            this.command = command;
        }

        public Builder setCid(String cid) {
            this.cid = cid;
            return this;
        }

        public Builder setJpushinfo(String jpushinfo) {
            this.jpushinfo = jpushinfo;
            return this;
        }

        public TioHandshake build() {
            return new TioHandshake(token,
                    handshakeKey,
                    cid,
                    command,
                    jpushinfo);
        }
    }
}
