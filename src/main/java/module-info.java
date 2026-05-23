module co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq {

    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq
        to javafx.fxml;

    opens co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base
        to javafx.fxml;
    opens co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.auth
        to javafx.fxml;
    opens co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante
        to javafx.fxml;
    opens co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.operador
        to javafx.fxml;
    opens co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin
        to javafx.fxml;

    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.auth;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.operador;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;
    exports co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.utils;
}