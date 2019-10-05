package io.hashimati.usersservices.security; 

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.reactivestreams.Publisher;

import io.hashimati.usersservices.domains.User;
import io.hashimati.usersservices.repository.UserRepository;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider  {

    @Inject
    private UserRepository userRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
       User user =  userRepository.findUserByUsernameAndPassword(
                authenticationRequest.getIdentity().toString(),
                authenticationRequest.getSecret().toString());
        if ( user !=null ) {
            return Flowable.just(new UserDetails(user.getUsername(),
                    Arrays.asList(user.getRoles()
                            .replace(" ", "")
                            .split(","))));
        }
        return Flowable.just(new AuthenticationFailed());
    }
}