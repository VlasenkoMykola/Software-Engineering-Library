"use strict";

const booksApp = {
  data() {
    return {
	books: [],
	sortFunc: (a, b) => { return a.title.localeCompare(b.title) },
    }
  },
  computed: {
    sortedBooks () {
      return this.books.sort(this.sortFunc);
    },
    resultsTo() {
	return this.books.length;
    },
  },

    mounted() {
        this.fetchBooks();
    },
    methods: {
        async fetchBooks() {
	    const queryString = window.location.search;
	    console.log('queryString='+queryString);

	    const urlParams = new URLSearchParams(queryString);
	    let queryParams = [];
	    let supported_params = [ 'id', 'author_id', 'title', 'pattern' ];
	    for (let i in supported_params) {
		const param = urlParams.get(supported_params[i]);
		if (param !== null) {
		    queryParams.push(supported_params[i]+'='+param);
		}
	    }
	    this.books = await fetchBooksData(queryParams);
	},
        authorURL(id) {
	    return "/authors.html?id=" + id;
	},
        bookURL(id) {
	    //return "/books.html?id=" + id;
	    return "/book-details.html?id=" + id;
	},
        sortAZ() {
            console.log('sortAZ');
	    this.sortFunc = (a, b) => { return a.title.localeCompare(b.title) }
	},
        sortZA() {
            console.log('sortZA');
	    this.sortFunc = (a, b) => { return b.title.localeCompare(a.title) }
	},
    },
};

Vue.createApp(booksApp).mount('#app');

