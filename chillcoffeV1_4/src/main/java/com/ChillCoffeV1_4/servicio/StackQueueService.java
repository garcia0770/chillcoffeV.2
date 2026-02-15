package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.CafeteriaItem;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;

@Service
public class StackQueueService {

    private final Stack<CafeteriaItem> pila = new Stack<>();
    private final Queue<CafeteriaItem> cola = new LinkedList<>();

    public void pushPila(CafeteriaItem item) {
        pila.push(item);
    }

    public void enqueueCola(CafeteriaItem item) {
        cola.add(item);
    }

    public Optional<CafeteriaItem> popPila() {
        if (!pila.isEmpty()) {
            return Optional.of(pila.pop());
        }
        return Optional.empty();
    }

    public Optional<CafeteriaItem> dequeueCola() {
        if (!cola.isEmpty()) {
            return Optional.of(cola.poll());
        }
        return Optional.empty();
    }

    public void limpiarPila() {
        pila.clear();
    }

    public void limpiarCola() {
        cola.clear();
    }

    public List<CafeteriaItem> obtenerPila() {
        return pila;
    }

    public List<CafeteriaItem> obtenerCola() {
        return new LinkedList<>(cola);
    }
}
