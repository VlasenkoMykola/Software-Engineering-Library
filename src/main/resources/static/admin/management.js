let currentPage = 1;
const itemsPerPage = 10;

const bookTableBody = document.getElementById("bookTableBody");
const searchInput = document.querySelector(".search input");

function openModal(modalId) {
  document.getElementById(modalId).classList.add("active");
}

function closeModal(modalId) {
  document.getElementById(modalId).classList.remove("active");
}

function renderBooks(booksData) {
  const start = (currentPage - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const paginatedBooks = booksData.slice(start, end);

  bookTableBody.innerHTML = paginatedBooks
    .map(
      (book) => `
        <tr>
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.type}</td>
            <td>${book.language}</td>
            <td>
                <span class="status ${
                  book.status === "в наявності" ? "available" : "borrowed"
                }">
                    ${book.status}
                </span>
            </td>
            <td class="actions-cell">
                <button class="action-btn edit-btn" onclick="openEditModal(${
                  book.id
                })">✏️</button>
                <button class="action-btn delete-btn" onclick="openDeleteModal(${
                  book.id
                })">🗑️</button>
            </td>
        </tr>
    `
    )
    .join("");
}

function openEditModal(bookId) {
  const book = books.find((b) => b.id === bookId);
  if (!book) return;

  const editForm = document.getElementById("editBookForm");
  editForm.querySelector('input[placeholder="Назва"]').value = book.title;
  editForm.querySelector('input[placeholder="Тип"]').value = book.type;

  openModal("editModal");
}

function openDeleteModal(bookId) {
  openModal("deleteModal");
  window.bookToDelete = bookId;
}

function confirmDelete() {
  if (window.bookToDelete) {
    const index = books.findIndex((b) => b.id === window.bookToDelete);
    if (index > -1) {
      books.splice(index, 1);
      renderBooks(books);
    }
    closeModal("deleteModal");
    window.bookToDelete = null;
  }
}

function filterBooks(searchTerm) {
  return books.filter((book) =>
    Object.values(book).some((value) =>
      value.toString().toLowerCase().includes(searchTerm.toLowerCase())
    )
  );
}

searchInput.addEventListener("input", (e) => {
  const searchTerm = e.target.value;
  const filteredBooks = filterBooks(searchTerm);
  renderBooks(filteredBooks);
});

document.getElementById("addBookForm").addEventListener("submit", (e) => {
  e.preventDefault();
  closeModal("addModal");
});

document.getElementById("editBookForm").addEventListener("submit", (e) => {
  e.preventDefault();
  closeModal("editModal");
});

document.addEventListener("DOMContentLoaded", () => {
  renderBooks(books);
});

const books = [
  {
    id: 1,
    title: "Загублена істота Z",
    type: "Пригодницький роман",
    language: "Українська",
    status: "в наявності",
  },
  {
    id: 2,
    title: "Цифрова фортеця",
    type: "Техно-трилер",
    language: "Українська",
    status: "позичений",
  },
  {
    id: 3,
    title: "Сто років самотності",
    type: "Магічний реалізм",
    language: "Українська",
    status: "в наявності",
  },
  {
    id: 4,
    title: "Sapiens: Коротка історія людства",
    type: "Науково-популярна література",
    language: "Українська",
    status: "позичений",
  },
  {
    id: 5,
    title: "Убити пересмішника",
    type: "Класична література",
    language: "Українська",
    status: "позичений",
  },
  {
    id: 6,
    title: "Нейромант",
    type: "Наукова фантастика",
    language: "Українська",
    status: "в наявності",
  },
  {
    id: 7,
    title: "Шерлок Холмс: Повне зібрання",
    type: "Детектив",
    language: "Українська",
    status: "в наявності",
  },
  {
    id: 8,
    title: "Гордість і упередження",
    type: "Романтика/Комедія",
    language: "Українська",
    status: "позичений",
  },
  {
    id: 9,
    title: "Код да Вінчі",
    type: "Містичний трилер",
    language: "English",
    status: "в наявності",
  },
  {
    id: 10,
    title: "1984",
    type: "Антиутопія",
    language: "Українська",
    status: "в наявності",
  },
];

document.addEventListener("DOMContentLoaded", () => {
  renderBooks(books);
});
