package com.example.miblocdenotas;

import java.util.Objects;

public class Nota {

    private final long id;
    private final String nota;
    private final String fechaCreacion;

    public Nota(long id, String nota, String fechaCreacion) {
        this.id = id;
        this.nota = nota;
        this.fechaCreacion = fechaCreacion;
    }

    public long getId() {
        return id;
    }

    public String getNota() {
        return nota;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return id == nota.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", nota='" + nota + '\'' +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                '}';
    }
}
