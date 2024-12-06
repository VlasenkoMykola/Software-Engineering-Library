const authorsApp = {
  data() {
    return {
	authors: [],
	sortFunc: (a, b) => { return a.full_name.localeCompare(b.full_name) },
    }
  },
  computed: {
    sortedAuthors () {
	return this.authors.sort(this.sortFunc);
    },
    resultsTo() {
	return this.authors.length;
    },
  },

    mounted() {
        this.fetchAuthors();
    },
    methods: {
        fetchAuthors() {

	    let url = '/backend/get_authors';
	    const queryString = window.location.search;
	    //console.log(queryString);
	    const urlParams = new URLSearchParams(queryString);
	    let queryParams = [];
	    let supported_params = [ 'id', 'letter', 'first_name', 'middle_name', 'last_name', 'full_name', 'pattern' ];
	    for (let i in supported_params) {
		const param = urlParams.get(supported_params[i]);
		if (param !== null) {
		    queryParams.push(supported_params[i]+'='+param);
		}
	    }
	    if (queryParams.length>0) {
		url += '?' + queryParams.join("&");
	    };
	    console.log('queryString='+queryString);
	    console.log('url='+url);


            fetch(url)
            .then(response => response.json())
            .then(data => {
		this.authors = data;
            })
            .catch(error => {
              console.error('Error fetching authors:', error);
            });
        },
        authorURL(id) {
	    return "/author.html?id=" + id;
	},
        sortAZ() {
            console.log('sortAZ');
	    this.sortFunc = (a, b) => { return a.full_name.localeCompare(b.full_name) }
	},
        sortZA() {
            console.log('sortZA');
	    this.sortFunc = (a, b) => { return b.full_name.localeCompare(a.full_name) }
	},
    },
};

Vue.createApp(authorsApp).mount('#app');

