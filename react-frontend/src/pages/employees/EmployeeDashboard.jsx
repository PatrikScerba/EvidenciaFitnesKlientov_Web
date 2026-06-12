import { useState } from "react";
import ClientRegister from "../clients/ClientRegister";
import { EMPLOYEE_VIEWS } from "../../constants/dashboardViews";
import ClientManagementSection from "../../components/clients/ClientManagementSection";
import ActiveEntriesDashboard from "../../components/entries/ActiveEntriesDashboard";
import QrServiceScanSection from "../../components/entries/QrServiceScanSection";
import QrTurnstileScanSection from "../../components/entries/QrTurnstileScanSection";

export default function EmployeeDashboard({ user }) {
  const [activeView, setActiveView] = useState(null);

  function toggleView(viewName) {
    setActiveView((prev) => (prev === viewName ? null : viewName));
  }

  return (
    <div>
      <h2>Zamestnanecký panel</h2>
      <p>Prihlásený ako zamestnanec: {user.email}</p>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(EMPLOYEE_VIEWS.CLIENT_REGISTER)}
      >
        {activeView === EMPLOYEE_VIEWS.CLIENT_REGISTER
          ? "Zavrieť registráciu"
          : "Registrovať klienta"}
      </button>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(EMPLOYEE_VIEWS.CLIENT_LIST)}
      >
        {activeView === EMPLOYEE_VIEWS.CLIENT_LIST
          ? "Zavrieť zoznam klientov"
          : "Zobraziť klientov"}
      </button>

      <button onClick={() => toggleView(EMPLOYEE_VIEWS.CLIENT_SEARCH)}>
        {activeView === EMPLOYEE_VIEWS.CLIENT_SEARCH
          ? "Zavrieť vyhľadávanie klientov"
          : "Vyhľadať klienta"}
      </button>

      <button onClick={() => toggleView(EMPLOYEE_VIEWS.ACTIVE_ENTRIES)}>
        {activeView === EMPLOYEE_VIEWS.ACTIVE_ENTRIES
          ? "Zavrieť aktuálne vstupy"
          : "Aktuálne v fitnescentre"}
      </button>

      <button
        style={{ marginLeft: "10px" }}
        onClick={() => toggleView(EMPLOYEE_VIEWS.QR_SERVICE_SCAN)}
      >
        {activeView === EMPLOYEE_VIEWS.QR_SERVICE_SCAN
          ? "Zavrieť servisný QR scan"
          : "Servisný QR scan"}
      </button>

      <button
        style={{ marginLeft: "10px" }}
        onClick={() => toggleView(EMPLOYEE_VIEWS.QR_TURNSTILE_SCAN)}
      >
        {activeView === EMPLOYEE_VIEWS.QR_TURNSTILE_SCAN
          ? "Zavrieť turniketový režim"
          : "Turniketový režim"}
      </button>

      <ClientManagementSection
        activeView={activeView}
        views={EMPLOYEE_VIEWS}
        userRole={user?.role}
      />

      {activeView === EMPLOYEE_VIEWS.CLIENT_REGISTER && (
        <div style={{ marginTop: "20px" }}>
          <ClientRegister />
        </div>
      )}

      {activeView === EMPLOYEE_VIEWS.ACTIVE_ENTRIES && (
        <ActiveEntriesDashboard />
      )}

      {activeView === EMPLOYEE_VIEWS.QR_SERVICE_SCAN && (
        <div style={{ marginTop: "20px" }}>
          <QrServiceScanSection />
        </div>
      )}
      {activeView === EMPLOYEE_VIEWS.QR_TURNSTILE_SCAN && (
        <div style={{ marginTop: "20px" }}>
          <QrTurnstileScanSection />
        </div>
      )}
    </div>
  );
}
