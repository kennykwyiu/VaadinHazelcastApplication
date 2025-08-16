package com.example.views;


import com.example.service.SessionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    private final SessionService sessionService;
    private final Div sessionInfo = new Div();
    private final TextField dataField = new TextField("Store in Session");
    private final Div storedData = new Div();

    @Autowired
    public MainView(SessionService sessionService) {
        this.sessionService = sessionService;

        setMargin(true);
        setPadding(true);
        setSpacing(true);

        createUI();
        updateSessionInfo();
    }

    private void createUI() {
        // Title
        add(new H1("Vaadin + Hazelcast Clustering Demo"));
        add(new Paragraph("This application demonstrates session clustering with Hazelcast. " +
                "Data stored in session is shared across all cluster nodes."));

        // Session Information
        add(new H2("Session Information"));
        add(sessionInfo);

        // Session Data Management
        add(new H2("Session Data"));
        dataField.setPlaceholder("Enter data to store in session");
        dataField.setWidth("300px");

        Button storeButton = new Button("Store Data", e -> {
            String data = dataField.getValue();
            if (!data.isEmpty()) {
                sessionService.storeData("user-data", data);
                dataField.clear();
                updateStoredData();
            }
        });

        Button clearButton = new Button("Clear Data", e -> {
            sessionService.clearData("user-data");
            updateStoredData();
        });

        Button refreshButton = new Button("Refresh", e -> {
            updateSessionInfo();
            updateStoredData();
        });

        add(dataField, storeButton, clearButton, refreshButton);
        add(new H2("Stored Data"));
        add(storedData);

        updateStoredData();
    }

    private void updateSessionInfo() {
        sessionInfo.removeAll();

        String sessionId = sessionService.getSessionId();
        String nodeInfo = sessionService.getNodeInfo();

        sessionInfo.add(new Paragraph("Session ID: " + sessionId));
        sessionInfo.add(new Paragraph("Hazelcast Node: " + nodeInfo));
        sessionInfo.add(new Paragraph("Session is shared across all cluster nodes via Hazelcast."));
    }

    private void updateStoredData() {
        storedData.removeAll();

        String data = sessionService.getData("user-data");
        if (data != null) {
            storedData.add(new Paragraph("Stored: " + data));
        } else {
            storedData.add(new Paragraph("No data stored in session"));
        }
    }
}
