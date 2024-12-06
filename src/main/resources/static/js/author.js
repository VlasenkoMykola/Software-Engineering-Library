const authorApp = {
  data() {
    return {
	author: {},
	bookCount: 0
    }
  },

    mounted() {
        this.fetchAuthor();
        this.fetchCount();
    },
    methods: {
        fetchAuthor() {
	    const queryString = window.location.search;
	    //console.log(queryString);
	    const urlParams = new URLSearchParams(queryString);
	    const id = urlParams.get('id');
            fetch('/backend/get_authors?id=' + id)
            .then(response => response.json())
            .then(data => {
		//console.log(data[0]);
		this.author = data[0];
            })
            .catch(error => {
              console.error('Error fetching author:', error);
            });
        },
        fetchCount() {
	    const queryString = window.location.search;
	    //console.log(queryString);
	    const urlParams = new URLSearchParams(queryString);
	    const id = urlParams.get('id');
            fetch('/backend/get_author_book_count?id=' + id)
            .then(response => response.json())
            .then(data => {
		//console.log('fetchCount...');
		//console.log(data);
		this.bookCount = data;
            })
            .catch(error => {
              console.error('Error fetching author book count:', error);
            });
        },
        booksURL(id) {
	    return "/books.html?author_id=" + id;
	},
    },
};

Vue.createApp(authorApp).mount('#app');

