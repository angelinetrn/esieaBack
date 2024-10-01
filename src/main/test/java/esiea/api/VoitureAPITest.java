package esiea.api;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import esiea.dao.ReponseVoiture;
import esiea.dao.VoitureDAO;
import esiea.metier.Voiture;
import java.sql.SQLException;

class VoitureAPITest {

    private VoitureAPI voitureAPI;
    private VoitureDAO voitureDAO;

    @BeforeEach
    void setUp() {
        voitureDAO = Mockito.mock(VoitureDAO.class);
        voitureAPI = new VoitureAPI();
        voitureAPI.setVoitureDAO(voitureDAO); // Utilisation du setter pour injecter le mock
    }

    @Test
    void testGetVoituresJson_all() {
        try {
            // Mocker la réponse de VoitureDAO
            ReponseVoiture reponse = new ReponseVoiture();
            Voiture voiture1 = new Voiture();
            voiture1.setMarque("Renault");
            voiture1.setModele("Clio");
            Voiture[] voitures = { voiture1 };
            reponse.setData(voitures);
            reponse.setVolume(1);

            Mockito.when(voitureDAO.getVoitures(null, 0, 10)).thenReturn(reponse);

            // Appeler la méthode à tester
            String result = voitureAPI.getVoituresJson("all", "0", "10");

            // Vérifier que le résultat contient la voiture correcte
            assertTrue(result.contains("Renault"));
            assertTrue(result.contains("Clio"));
        } catch (SQLException e) {
            fail("SQLException should not have been thrown");
        }
    }

    @Test
    void testAjouterVoiture_succes() {
        try {
            String voitureJson = "{ \"marque\": \"Lambo\", \"modele\": \"208\", \"finition\": \"Allure\", \"carburant\": \"ESSENCE\", \"km\": 12000, \"annee\": 2020, \"prix\": 15000 }";

            Mockito.doNothing().when(voitureDAO).ajouterVoiture(Mockito.any(Voiture.class));
            String result = voitureAPI.ajouterVoiture(voitureJson);
            assertTrue(result.contains("\"succes\":false"));
        } catch (SQLException e) {
            fail("SQLException should not have been thrown");
        }
    }
}
