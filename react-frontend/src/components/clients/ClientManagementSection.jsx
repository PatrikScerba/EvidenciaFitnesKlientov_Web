import { useEffect, useState, useRef } from "react";
import ClientList from "../../pages/clients/ClientList";
import ClientSearch from "../../pages/clients/ClientSearch";
import ClientEdit from "../../pages/clients/ClientEdit";
import ClientResultsTable from "./ClientResultsTable";
import { searchClients, deleteClient } from "../../api/clientApi";
import MembershipManagementSection from "../memberships/MembershipManagementSection";
import EntryManagementSection from "../entries/EntryManagementSection";
import AdminClientQrSection from "../qr/AdminClientQrSection";
import EmployeeClientQrSection from "../qr/EmployeeClientQrSection";
import SendEmailModal from "../email/SendEmailModal";

export default function ClientManagementSection({
  activeView,
  views,
  userRole,
}) {
  const [searchResults, setSearchResults] = useState([]);
  const [loadingSearch, setLoadingSearch] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [editingClient, setEditingClient] = useState(null);
  const [clientListRefresh, setClientListRefresh] = useState(0);
  const [membershipClient, setMembershipClient] = useState(null);

  const isSearchView = activeView === views.CLIENT_SEARCH;
  const isListView = activeView === views.CLIENT_LIST;
  const showEmailSelectionControls =
    isListView || (isSearchView && hasSearched && searchResults.length > 0);
  const isAdmin = userRole === "ADMIN";
  const isEmployee = userRole === "EMPLOYEE";
  const showEditForm = editingClient && (isSearchView || isListView);

  const [entryClient, setEntryClient] = useState(null);

  const [qrClient, setQrClient] = useState(null);
  const [emailModal, setEmailModal] = useState(null);
  const [emailSending, setEmailSending] = useState(false);
  const emailSendingRef = useRef(false);
  const [isEmailSelectionMode, setIsEmailSelectionMode] = useState(false);
  const [selectedClients, setSelectedClients] = useState([]);

  const selectedClientIds = selectedClients.map((client) => client.clientId);

  useEffect(() => {
    if (emailSendingRef.current) return;

    setEditingClient(null);
    setMembershipClient(null);
    setEntryClient(null);
    setQrClient(null);
    setEmailModal(null);
    setIsEmailSelectionMode(false);
    setSelectedClients([]);
  }, [activeView]);

  function handleEditClient(client) {
    setEditingClient(client);
    setMembershipClient(null);
    setEntryClient(null);
    setQrClient(null);
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
    setEntryClient(null);
    setQrClient(null);
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
      closeEntryForm();
      setSelectedClients([]);
      setIsEmailSelectionMode(false);

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

      setSelectedClients((prev) =>
        prev.filter((client) => client.clientId !== id)
      );

      setSearchResults((prev) =>
        prev.filter((client) => client.clientId !== id)
      );
      setEntryClient(null);
      setQrClient(null);
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
    setQrClient(null);
    setEntryClient(null);
  }

  function closeMembershipForm() {
    setMembershipClient(null);
  }

  function handleEntryClient(client) {
    setEntryClient(client);
    setEditingClient(null);
    setMembershipClient(null);
    setQrClient(null);
  }

  function closeEntryForm() {
    setEntryClient(null);
  }

  function handleShowQr(client) {
    setQrClient(client);
    setEditingClient(null);
    setMembershipClient(null);
    setEntryClient(null);
  }

  function closeQrSection() {
    setQrClient(null);
  }

  function handleSendEmail(client) {
    if (emailSendingRef.current) return;

    setEmailModal({
      mode: "single",
      client: client,
    });
  }

  function handleSendEmailAll() {
    if (emailSendingRef.current) return;

    setIsEmailSelectionMode(false);
    setSelectedClients([]);

    setEmailModal({
      mode: "all",
      client: null,
    });
  }

  function handleEmailSendingChange(sending) {
    emailSendingRef.current = sending;
    setEmailSending(sending);
  }

  function closeEmailModal() {
    setEmailModal(null);
  }

  function handleEmailSuccess() {
    setEmailModal(null);
    setSelectedClients([]);
    setIsEmailSelectionMode(false);
  }

  function handleToggleEmailSelection() {
    if (emailSendingRef.current) return;

    setIsEmailSelectionMode((prev) => !prev);
    setSelectedClients([]);
    setEmailModal(null);
  }

  function handleToggleClientSelection(client) {
    setSelectedClients((prev) =>
      prev.some((c) => c.clientId === client.clientId)
        ? prev.filter((c) => c.clientId !== client.clientId)
        : [...prev, client]
    );
  }

  function handleContinueSelectedEmail() {
    if (emailSendingRef.current) return;

    if (selectedClientIds.length === 0) return;

    setEmailModal({
      mode: "selected",
      client: null,
    });
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
                  onManageEntry={handleEntryClient}
                  onShowQr={handleShowQr}
                  onSendEmail={handleSendEmail}
                  isEmailSelectionMode={isEmailSelectionMode}
                  selectedClientIds={selectedClientIds}
                  onToggleClientSelection={handleToggleClientSelection}
                />
              ))}
          </div>
        </div>
      )}

      {isListView && (
        <div style={{ marginTop: "20px" }}>
          <ClientList
            onShowQr={handleShowQr}
            onEdit={handleEditClient}
            onSendEmail={handleSendEmail}
            onManageMembership={handleManageMembership}
            onManageEntry={handleEntryClient}
            isEmailSelectionMode={isEmailSelectionMode}
            selectedClientIds={selectedClientIds}
            onToggleClientSelection={handleToggleClientSelection}
            onDeleteSuccess={() => {
              closeEditForm();
              refreshClientList();
            }}
            refreshKey={clientListRefresh}
          />

          <button
            type="button"
            onClick={handleSendEmailAll}
            disabled={emailSending}
          >
            Odoslať email všetkým klientom
          </button>
        </div>
      )}

      {showEmailSelectionControls && (
        <div style={{ marginTop: "12px" }}>
          <button
            type="button"
            onClick={handleToggleEmailSelection}
            disabled={emailSending}
          >
            {isEmailSelectionMode
              ? "Zrušiť výber"
              : "Odoslať email vybraným klientom"}
          </button>

          {isEmailSelectionMode && (
            <>
              <p>Počet vybraných klientov: {selectedClientIds.length}</p>

              <button
                type="button"
                onClick={handleContinueSelectedEmail}
                disabled={emailSending || selectedClientIds.length === 0}
              >
                Pokračovať na odoslanie
              </button>
            </>
          )}
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
      {entryClient && (
        <div style={{ marginTop: "20px" }}>
          <EntryManagementSection
            client={entryClient}
            onClose={closeEntryForm}
          />
        </div>
      )}
      {qrClient && isAdmin && (
        <div style={{ marginTop: "20px" }}>
          <AdminClientQrSection client={qrClient} onClose={closeQrSection} />
        </div>
      )}
      {qrClient && isEmployee && (
        <div style={{ marginTop: "20px" }}>
          <EmployeeClientQrSection client={qrClient} onClose={closeQrSection} />
        </div>
      )}

      {emailModal && (
        <SendEmailModal
          key={
            emailModal.mode === "single"
              ? emailModal.client.clientId
              : emailModal.mode
          }
          mode={emailModal.mode}
          client={emailModal.client}
          selectedClientIds={
            emailModal.mode === "selected" ? selectedClientIds : []
          }
          selectedClients={
            emailModal.mode === "selected" ? selectedClients : []
          }
          onSendingChange={handleEmailSendingChange}
          onClose={closeEmailModal}
          onSuccess={handleEmailSuccess}
        />
      )}
    </>
  );
}
