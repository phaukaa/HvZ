package no.noroff.hvz.security;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.util.Base64;

public class SecurityUtils {

    public static boolean isAdmin(String bearerToken) {
        try {
            JSONObject payload = getTokenPayload(bearerToken);
            return ((String) payload.get("scope")).matches(".*admin:permissions.*");
        } catch (Exception exception) {
            return false;
        }
    }

    public static JSONObject getTokenPayload(String bearerToken) throws Exception {
        String token = bearerToken.substring(7);

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        return new JSONObject(JSONObjectUtils.parse(payload));
    }

    public static JSONObject getTokenHeaders(String bearerToken) throws Exception {
        String token = bearerToken.substring(7);

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();

        String header = new String(decoder.decode(chunks[0]));
        return new JSONObject(JSONObjectUtils.parse(header));
    }
}
