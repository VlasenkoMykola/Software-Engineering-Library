const mockData = {
  borrowed: [
    {
      id: "001",
      userId: "001",
      amount: "002 Books",
      dueDate: "13 - 03 - 2024",
      dateTime: "25-02-2024, 10:39:43",
      title: "Загублене місто Z",
      type: "Пригодницький роман",
      language: "Українська",
      availability: "з 21.02.2001 до 22.02.2001",
    },
    // More data...
  ],
  overdue: [
    {
      id: "001",
      userId: "1",
      amount: "3",
      dueDate: "13 - 03 - 2024",
      dateTime: "25-02-2024, 10:39:43",
      title: "Загублене місто Z",
      type: "Пригодницький роман",
      language: "Українська",
      availability: "з 21.02.2001 до 22.02.2001",
    },
    // More data...
  ],
};

let currentTab = "borrowed";
let currentPage = 1;
const itemsPerPage = 10;

const tableBody = document.getElementById("tableBody");
const searchInput = document.getElementById("searchInput");
const tabButtons = document.querySelectorAll(".tab-btn");
const paginationDots = document.querySelectorAll(".pagination-dot");

function setTableHeaders(tab) {
  const headerRow = document.querySelector("thead tr");
  const headers =
    tab === "borrowed"
      ? ["Назва", "Тип", "Мова", "Наявність"]
      : ["Назва", "Тип", "Мова", "Наявність"];

  headerRow.innerHTML = headers.map((header) => `<th>${header}</th>`).join("");
}

function renderTable(data) {
  const start = (currentPage - 1) * itemsPerPage;
  const end = currentPage * itemsPerPage;
  const currentData = data.slice(start, end);

  tableBody.innerHTML = currentData
    .map(
      (item) => `
    <tr>
      <td>${item.id}</td>
      <td>${item.userId || item.readerId}</td>
      <td>${item.amount}</td>
      <td>${item.dueDate}</td>
      <td>${item.dateTime}</td>
      <td><button class="action-btn" onclick="showModal('${
        item.id
      }')"><svg width="17" height="20" viewBox="0 0 17 20" fill="none" xmlns="http://www.w3.org/2000/svg">
<path d="M16.5 1.67C16.5 1.66 16.5 1.65 16.48 1.64C16.26 1.36 15.97 1.21 15.63 1.21C15.1 1.21 14.46 1.56 13.77 2.3C12.95 3.18 11.69 3.11 10.97 2.15L9.96 0.81C9.56 0.27 9.03 0 8.5 0C7.97 0 7.44 0.27 7.04 0.81L6.02 2.16C5.31 3.11 4.06 3.18 3.24 2.31L3.23 2.3C2.1 1.09 1.09 0.91 0.52 1.64C0.5 1.65 0.5 1.66 0.5 1.67C0.14 2.44 0 3.52 0 5.04V14.96C0 16.48 0.14 17.56 0.5 18.33C0.5 18.34 0.51 18.36 0.52 18.37C1.1 19.09 2.1 18.91 3.23 17.7L3.24 17.69C4.06 16.82 5.31 16.89 6.02 17.84L7.04 19.19C7.44 19.73 7.97 20 8.5 20C9.03 20 9.56 19.73 9.96 19.19L10.97 17.85C11.69 16.89 12.95 16.82 13.77 17.7C14.46 18.44 15.1 18.79 15.63 18.79C15.97 18.79 16.26 18.65 16.48 18.37C16.49 18.36 16.5 18.34 16.5 18.33C16.86 17.56 17 16.48 17 14.96V5.04C17 3.52 16.86 2.44 16.5 1.67ZM11 12.5H5C4.59 12.5 4.25 12.16 4.25 11.75C4.25 11.34 4.59 11 5 11H11C11.41 11 11.75 11.34 11.75 11.75C11.75 12.16 11.41 12.5 11 12.5ZM13 9H5C4.59 9 4.25 8.66 4.25 8.25C4.25 7.84 4.59 7.5 5 7.5H13C13.41 7.5 13.75 7.84 13.75 8.25C13.75 8.66 13.41 9 13 9Z" fill="black"/>
</svg>
</button></td>
    </tr>
  `
    )
    .join("");
}

function showModal(itemId) {
  const item = mockData[currentTab].find((item) => item.id === itemId);
  document.getElementById("modal-title").innerText = item.title;
  document.getElementById("modal-type").innerText = item.type;
  document.getElementById("modal-language").innerText = item.language;
  document.getElementById("modal-availability").innerText = item.availability;
  document.getElementById("modal").style.display = "flex";
}

function closeModal() {
  document.getElementById("modal").style.display = "none";
}

function updatePagination() {
  paginationDots.forEach((dot) => dot.classList.remove("active"));
  paginationDots[currentPage - 1].classList.add("active");
}

function switchTab(tab) {
  currentTab = tab;
  setTableHeaders(tab);
  renderTable(mockData[tab]);
}

function searchTable() {
  const query = searchInput.value.toLowerCase();
  const filteredData = mockData[currentTab].filter(
    (item) =>
      item.id.toLowerCase().includes(query) ||
      item.userId.toLowerCase().includes(query) ||
      item.amount.toLowerCase().includes(query) ||
      item.dueDate.toLowerCase().includes(query)
  );
  renderTable(filteredData);
}

searchInput.addEventListener("input", searchTable);

document.querySelectorAll(".tab-btn").forEach((button) => {
  button.addEventListener("click", () => {
    const tab = button.dataset.tab;
    switchTab(tab);
  });
});

paginationDots.forEach((dot) => {
  dot.addEventListener("click", () => {
    currentPage = parseInt(dot.dataset.page);
    updatePagination();
    renderTable(mockData[currentTab]);
  });
});

// Initialize table with borrowed data
switchTab("borrowed");

tabButtons.forEach((button) => {
  button.addEventListener("click", (event) => {
    currentTab = event.target.dataset.tab; // Switch tab based on data-tab attribute
    tabButtons.forEach((btn) => btn.classList.remove("active")); // Remove active class from all tabs
    event.target.classList.add("active"); // Add active class to clicked tab
    currentPage = 1; // Reset to page 1
    renderTable(mockData[currentTab]); // Render the table based on selected tab
  });
});
