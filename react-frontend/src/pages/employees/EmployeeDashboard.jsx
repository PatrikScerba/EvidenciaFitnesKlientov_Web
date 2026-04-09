import { useState } from "react";
import ClientRegister from "../clients/ClientRegister";
import { EMPLOYEE_VIEWS } from "../../constants/dashboardViews";
import ClientManagementSection from "../../components/clients/ClientManagementSection";

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

      <ClientManagementSection activeView={activeView} views={EMPLOYEE_VIEWS} />

      {activeView === EMPLOYEE_VIEWS.CLIENT_REGISTER && (
        <div style={{ marginTop: "20px" }}>
          <ClientRegister />
        </div>
      )}
    </div>
  );
}
