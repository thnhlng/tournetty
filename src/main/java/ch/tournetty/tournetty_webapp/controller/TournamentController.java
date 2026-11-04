package ch.tournetty.tournetty_webapp.controller;

import ch.tournetty.tournetty_webapp.api.TournamentsApi;
import ch.tournetty.tournetty_webapp.api.model.Tournament;
import ch.tournetty.tournetty_webapp.api.model.TournamentCreateRequest;
import ch.tournetty.tournetty_webapp.api.model.TournamentStatus;
import ch.tournetty.tournetty_webapp.api.model.TournamentUpdateRequest;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController implements TournamentsApi {

    @GetMapping("/tournament/api/hello")
    public String helloWorld() {
        return "Hello World";
    }


    @Override
    public ResponseEntity<List<Tournament>> listTournaments(@Nullable Set<TournamentStatus> status) {
        return notImplemented();
    }

    @Override
    public ResponseEntity<Tournament> createTournament(TournamentCreateRequest tournamentCreateRequest) {
        return notImplemented();
    }

    @Override
    public ResponseEntity<Tournament> getTournament(Long tournamentId) {
        return notImplemented();
    }

    @Override
    public ResponseEntity<Tournament> updateTournament(Long tournamentId,
                                                       TournamentUpdateRequest tournamentUpdateRequest) {
        return notImplemented();
    }

    @Override
    public ResponseEntity<Void> deleteTournament(Integer version, Long tournamentId) {
        return notImplemented();
    }

    private static <T> ResponseEntity<T> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
