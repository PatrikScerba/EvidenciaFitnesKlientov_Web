import { useState } from "react";
import ClientRegister from "../clients/ClientRegister";
import EmployeeRegister from "../employees/EmployeeRegister";
import AdminPasswordReset from "./AdminPasswordReset";
import { ADMIN_VIEWS } from "../../constants/dashboardViews";
import ClientManagementSection from "../../components/clients/ClientManagementSection";
import ActiveEntriesDashboard from "../../components/entries/ActiveEntriesDashboard";

export default function AdminDashboard({ user }) {
  const [activeView, setActiveView] = useState(null);

  function toggleView(viewName) {
    setActiveView((prev) => (prev === viewName ? null : viewName));
  }

  return (
    <div>
      <h2>Admin panel</h2>
      <p>Prihlásený ako admin: {user.email}</p>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(ADMIN_VIEWS.EMPLOYEE_REGISTER)}
      >
        {activeView === ADMIN_VIEWS.EMPLOYEE_REGISTER
          ? "Zavrieť registráciu zamestnanca"
          : "Registrovať zamestnanca"}
      </button>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(ADMIN_VIEWS.CLIENT_REGISTER)}
      >
        {activeView === ADMIN_VIEWS.CLIENT_REGISTER
          ? "Zavrieť registráciu klienta"
          : "Registrovať klienta"}
      </button>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(ADMIN_VIEWS.PASSWORD_RESET)}
      >
        {activeView === ADMIN_VIEWS.PASSWORD_RESET
          ? "Zavrieť reset hesla"
          : "Reset hesla používateľa"}
      </button>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(ADMIN_VIEWS.CLIENT_LIST)}
      >
        {activeView === ADMIN_VIEWS.CLIENT_LIST
          ? "Zavrieť zoznam klientov"
          : "Zobraziť klientov"}
      </button>

      <button
        style={{ marginRight: "10px" }}
        onClick={() => toggleView(ADMIN_VIEWS.CLIENT_SEARCH)}
      >
        {activeView === ADMIN_VIEWS.CLIENT_SEARCH
          ? "Zavrieť vyhľadávanie klientov"
          : "Vyhľadať klienta"}
      </button>

      <button onClick={() => toggleView(ADMIN_VIEWS.ACTIVE_ENTRIES)}>
        {activeView === ADMIN_VIEWS.ACTIVE_ENTRIES
          ? "Zavrieť aktuálne vstupy"
          : "Aktuálne v fitnescentre"}
      </button>

      <ClientManagementSection activeView={activeView} views={ADMIN_VIEWS} />

      {activeView === ADMIN_VIEWS.EMPLOYEE_REGISTER && (
        <div style={{ marginTop: "20px" }}>
          <EmployeeRegister />
        </div>
      )}

      {activeView === ADMIN_VIEWS.CLIENT_REGISTER && (
        <div style={{ marginTop: "20px" }}>
          <ClientRegister />
        </div>
      )}

      {activeView === ADMIN_VIEWS.PASSWORD_RESET && (
        <div style={{ marginTop: "20px" }}>
          <AdminPasswordReset />
        </div>
      )}

      {activeView === ADMIN_VIEWS.ACTIVE_ENTRIES && (
        <div style={{ marginTop: "20px" }}>
          <ActiveEntriesDashboard />
        </div>
      )}
    </div>
  );
}
