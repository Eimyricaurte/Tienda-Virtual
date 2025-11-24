package uptc.co.tienda.Services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JWT {

    private static final String secretKey = "bXktc2VjdXJlLXNlY3JldC1rZXktYmFzZTY0LWF1dGg=";

    public String validarToken(String authHeader) {

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new RuntimeException("Token faltante ");
    }

    String token = authHeader.substring(7);

    try {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    } catch (Exception e) {
        throw new RuntimeException("Token inválido o expirado");
    }
}

}
