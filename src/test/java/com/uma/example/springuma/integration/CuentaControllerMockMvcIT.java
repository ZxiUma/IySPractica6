package com.uma.example.springuma.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Cuenta;
import com.uma.example.springuma.model.Persona;

/**
 * Pruebas de integración para CuentaController usando MockMvc.
 *
 * Cubre los flujos principales del CRUD de cuentas y la relación Cuenta → Persona.
 */
class CuentaControllerMockMvcIT extends AbstractIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Persona persona;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() throws Exception {
        // Crear una persona titular para las cuentas
        persona = new Persona();
        persona.setNombre("Titular");
        persona.setDni("99887766A");
        persona.setEdad(30);

        mockMvc.perform(post("/persona")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isCreated());

        // Cuenta base para cada test
        cuenta = new Cuenta();
        cuenta.setId(1);
        cuenta.setCcc(100001);
        cuenta.setBalance(500.0);
    }

    // ------------------------------------------------------------------ //
    //  GET /cuentas
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("La lista de cuentas está vacía al inicio")
    void getCuentas_inicialmenteVacia() throws Exception {
    // TODO: implementa aquí el test
        this.mockMvc.perform(get("/cuentas")
                .accept("application/json"))
                .andExpect(content().json("[]"))
                .andExpect(status().isOk());
    }

    // ------------------------------------------------------------------ //
    //  POST /cuenta  →  GET /cuenta/{id}
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("Crear una cuenta y recuperarla por ID")
    void crearCuenta_seRecuperaPorId() throws Exception {
        // TODO: implementa aquí el test
        String cuentaJson = objectMapper.writeValueAsString(cuenta);

        this.mockMvc.perform(post("/cuenta")
                .contentType("application/json")
                .content(cuentaJson))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/cuenta/1")
                .accept("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ccc").value(100001))
                .andExpect(status().isOk());
        
    }

    @Test
    @DisplayName("Crear dos cuentas distintas: el listado contiene ambas")
    void crearDosCuentas_listaContieneDos() throws Exception {
        // TODO: implementa aquí el test
        // 1. ARRANGE: Preparamos dos objetos cuenta diferentes
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setId(2);
        cuenta2.setCcc(200002);
        cuenta2.setBalance(1000.0);

        // 2. ACT: Realizamos los dos POST
        this.mockMvc.perform(post("/cuenta")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cuenta))) // Usamos la del @BeforeEach
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/cuenta")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cuenta2)))
                .andExpect(status().isOk());

        // 3. ASSERT: Verificamos el GET /cuentas
        this.mockMvc.perform(get("/cuentas")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Verificamos que hay exactamente 2 cuentas
                .andExpect(jsonPath("$[0].ccc").value(100001)) // Primera cuenta
                .andExpect(jsonPath("$[1].ccc").value(200002)); // Segunda cuenta
    }

    // ------------------------------------------------------------------ //
    //  DELETE /cuenta
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("Crear y eliminar una cuenta: el listado queda vacío")
    void crearYEliminarCuenta_listaQuedaVacia() throws Exception {
        // TODO: implementa aquí el test
        // 1. ARRAGE: Creamos la cuenta primero
        String cuentaJson = objectMapper.writeValueAsString(cuenta);
        this.mockMvc.perform(post("/cuenta")
                .contentType("application/json")
                .content(cuentaJson))
                .andExpect(status().isOk());

        // 2. ACT: Eliminamos la cuenta (suponiendo que la ruta es /cuenta/{id})
        this.mockMvc.perform(delete("/cuenta")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(cuenta)))
            .andExpect(status().isOk());;

        // 3. ASSERT: Verificamos que el listado vuelve a estar vacío
        this.mockMvc.perform(get("/cuentas")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
