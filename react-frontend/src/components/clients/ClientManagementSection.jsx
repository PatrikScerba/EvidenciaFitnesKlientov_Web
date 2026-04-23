import { useEffect, useState } from "react";
import ClientList from "../../pages/clients/ClientList";
import ClientSearch from "../../pages/clients/ClientSearch";
import ClientEdit from "../../pages/clients/ClientEdit";
import ClientResultsTable from "./ClientResultsTable";
import { searchClients, deleteClient } from "../../api/clientApi";
import MembershipManagementSection from "../memberships/MembershipManagementSection";

export default function ClientManagementSection({ activeView, views }) {
  const [searchResults, setSearchResults] = useState([]);
  const [loadingSearch, setLoadingSearch] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [editingClient, setEditingClient] = useState(null);
  const [clientListRefresh, setClientListRefresh] = useState(0);
  const [membershipClient, setMembershipClient] = useState(null);

  const isSearchView = activeView === views.CLIENT_SEARCH;
  const isListView = activeView === views.CLIENT_LIST;
  const showEditForm = editingClient && (isSearchView || isListView);

  useEffect(() => {
    setEditingClient(null);
    setMembershipClient(null);
  }, [activeView]);

  function handleEditClient(client) {
    setEditingClient(client);
    setMembershipClient(null);
  }

  function closeEditForm() {
    setEditingClient(null);
  }

  function refreshClientList() {
    setClientListRefresh((prev) => prev + 1);
  }

  function resetSearchState() {
    setSearchResults([]);
    setHasSearched(false);
    closeEditForm();
    closeMembershipForm();
  }

  async function handleClientSearch(filters) {
    if (filters === null) {
      resetSearchState();
      return;
    }

    try {
      setLoadingSearch(true);
      setHasSearched(true);
      closeEditForm();
      closeMembershipForm();

      const data = await searchClients(filters);
      setSearchResults(data);
    } catch (err) {
      console.error("Vyhľadávanie klientov zlyhalo:", err);
      setSearchResults([]);
    } finally {
      setLoadingSearch(false);
    }
  }

  async function handleDeleteClient(id) {
    const confirmed = window.confirm("Naozaj chcete vymazať klienta?");
    if (!confirmed) return;

    try {
      await deleteClient(id);

      setSearchResults((prev) =>
        prev.filter((client) => client.clientId !== id)
      );

      closeEditForm();
      closeMembershipForm();
      refreshClientList();
    } catch (err) {
      console.error("Mazanie klienta zlyhalo:", err);
    }
  }

  function handleClientUpdated(updatedClient) {
    if (updatedClient) {
      setSearchResults((prev) =>
        prev.map((client) =>
          client.clientId === updatedClient.clientId ? updatedClient : client
        )
      );
    }

    closeEditForm();
    refreshClientList();
  }

  function handleManageMembership(client) {
    setEditingClient(null);
    setMembershipClient(client);
  }

  function closeMembershipForm() {
    setMembershipClient(null);
  }

  return (
    <>
      {isSearchView && (
        <div style={{ marginTop: "20px" }}>
          <ClientSearch onSearch={handleClientSearch} loading={loadingSearch} />

          <div style={{ marginTop: "20px" }}>
            {hasSearched &&
              (searchResults.length === 0 ? (
                <p>Nenašli sa žiadni klienti.</p>
              ) : (
                <ClientResultsTable
                  clients={searchResults}
                  onEdit={handleEditClient}
                  onDelete={handleDeleteClient}
                  onManageMembership={handleManageMembership}
                />
              ))}
          </div>
        </div>
      )}

      {isListView && (
        <div style={{ marginTop: "20px" }}>
          <ClientList
            onEdit={handleEditClient}
            onManageMembership={handleManageMembership}
            onDeleteSuccess={() => {
              closeEditForm();
              refreshClientList();
            }}
            refreshKey={clientListRefresh}
          />
        </div>
      )}

      {showEditForm && (
        <div style={{ marginTop: "20px" }}>
          <ClientEdit
            client={editingClient}
            onCancel={closeEditForm}
            onUpdated={handleClientUpdated}
          />
        </div>
      )}
      {membershipClient && (
        <div style={{ marginTop: "20px" }}>
          <MembershipManagementSection
            client={membershipClient}
            onClose={closeMembershipForm}
          />
        </div>
      )}
    </>
  );
}
