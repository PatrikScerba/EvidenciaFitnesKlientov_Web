import ClientDetail from "../clients/ClientDetail";

export default function ClientDashboard({ user }) {
  return (
    <div>
      <h2>Klientsky panel</h2>
      <p>Prihlásený klient: {user.email}</p>
      <ClientDetail />
    </div>
  );
}
