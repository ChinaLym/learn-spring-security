package demo.lym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JWK 配置
 *
 * 注意：授权服务器通常具有密钥轮换策略，并且密钥不会硬编码到应用程序代码中。
 *
 * 但为简单起见，本 demo 不演示 key 交换
 */
@Configuration
class KeyConfig {

	public ClientDetailsService clientDetailsService() {
		InMemoryClientDetailsService memoryClientDetailsService = new InMemoryClientDetailsService();
		Map<String, ? extends ClientDetails> clientDetails = new HashMap<>();
		BaseClientDetails demo;

		demo = new BaseClientDetails();
		demo.setClientId("demo-client-id");
		demo.setClientSecret("secret");
		demo.setAuthorizedGrantTypes(List.of("authorization_code", "password", "client_credentials", "implicit", "refresh_token"));
		demo.setRegisteredRedirectUri(Set.of("http://localhost:8080/login/oauth2/code/demo","http://127.0.0.1:8080/login/oauth2/code/demo"));
		demo.setScope(List.of("message:read", "message:write", "user:read"));
		demo.setAccessTokenValiditySeconds(600_000_000);

		memoryClientDetailsService.setClientDetailsStore(clientDetails);
		return memoryClientDetailsService;
	}

	@Bean
    KeyPair keyPair() {
		try {
			String privateExponent = "3851612021791312596791631935569878540203393691253311342052463788814433805390794604753109719790052408607029530149004451377846406736413270923596916756321977922303381344613407820854322190592787335193581632323728135479679928871596911841005827348430783250026013354350760878678723915119966019947072651782000702927096735228356171563532131162414366310012554312756036441054404004920678199077822575051043273088621405687950081861819700809912238863867947415641838115425624808671834312114785499017269379478439158796130804789241476050832773822038351367878951389438751088021113551495469440016698505614123035099067172660197922333993";
			String modulus = "18044398961479537755088511127417480155072543594514852056908450877656126120801808993616738273349107491806340290040410660515399239279742407357192875363433659810851147557504389760192273458065587503508596714389889971758652047927503525007076910925306186421971180013159326306810174367375596043267660331677530921991343349336096643043840224352451615452251387611820750171352353189973315443889352557807329336576421211370350554195530374360110583327093711721857129170040527236951522127488980970085401773781530555922385755722534685479501240842392531455355164896023070459024737908929308707435474197069199421373363801477026083786683";
			String exponent = "65537";

			RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent));
			RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return new KeyPair(factory.generatePublic(publicSpec), factory.generatePrivate(privateSpec));
		} catch ( Exception e ) {
			throw new IllegalArgumentException(e);
		}
	}
}
