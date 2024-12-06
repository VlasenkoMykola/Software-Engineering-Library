"use strict";

const bookApp = {
  data() {
    return {
	book: {},
    }
  },
    mounted() {
        this.fetchBook();
    },
    methods: {
        async fetchBook() {
	    const queryString = window.location.search;
	    console.log('queryString='+queryString);

	    const urlParams = new URLSearchParams(queryString);
	    const queryParams = [];
	    const supported_params = [ 'id' ];
	    for (let i in supported_params) {
		const param = urlParams.get(supported_params[i]);
		if (param !== null) {
		    queryParams.push(supported_params[i]+'='+param);
		}
	    }
	    const books = await fetchBooksData(queryParams);
	    const book = books[0];

	    const copiesPromise = fetch('/backend/get_copy_count?book_id='+book.id)
            .then(response => response.json())
            .catch(error => {
              console.error('Error fetching copy count:', error);
            });
	    const copies = await copiesPromise;
	    book.copies = copies;
	    this.book = book;
	},
        authorURL(id) {
	    return "/authors.html?id=" + id;
	},
    },
};

Vue.createApp(bookApp).mount('#app');

