"use strict";

const instancesApp = {
  data() {
    return {
	instances: [],
	sortFunc: (a, b) => { return a.title.localeCompare(b.title) },
    }
  },
  computed: {
    sortedInstances () {
      return this.instances.sort(this.sortFunc);
    },
    resultsTo() {
	return this.instances.length;
    },
  },

    mounted() {
        this.fetchInstances();
    },
    methods: {
        async fetchInstances() {
	    let instances_url = '/backend/get_copies';
	    let instance_books_url = '/backend/get_books_for_copies';
	    const queryString = window.location.search;
	    console.log('queryString='+queryString);

	    const urlParams = new URLSearchParams(queryString);
	    let queryParams = [];
	    let supported_params = [ 'id', 'book_id', 'reader_id', 'status' ];
	    for (let i in supported_params) {
		const param = urlParams.get(supported_params[i]);
		if (param !== null) {
		    queryParams.push(supported_params[i]+'='+param);
		}
	    }
	    if (queryParams.length>0) {
		const addOn = '?' + queryParams.join("&");
		instances_url += addOn;
		instance_books_url += addOn;
	    };
	    console.log('instances_url='+instances_url);
	    console.log('instance_books_url='+instance_books_url);

	    const instancesPromise = fetch(instances_url)
            .then(response => response.json())
            .catch(error => {
              console.error('Error fetching instances:', error);
            });
	    const instanceBooksPromise = fetch(instance_books_url)
            .then(response => response.json())
            .catch(error => {
              console.error('Error fetching instance books:', error);
            });
	    const instanceReadersPromise = fetch('/backend/get_reader_map_id_to_name')
            .then(response => response.json())
            .catch(error => {
              console.error('Error fetching reader id2name map:', error);
            });

	    let readerid2readers = await instanceReadersPromise;
	    if (readerid2readers === undefined) readerid2readers = {};
	    //console.log(readerid2readers);

	    let bookid2books = await instanceBooksPromise;
	    if (bookid2books === undefined) bookid2books = {};
	    //console.log(bookid2books);

	    let data = await instancesPromise;
	    //console.log(data);
	    for (let i in data) {
		//console.log('i=', i, ' bookid=', data[i].book_id);
		data[i].title = bookid2books[data[i].book_id];
		//const copy_id = data[i].id;
		const reader_id = data[i].reader_id;
		//console.log('i=', i, ' reader_id=', reader_id);
		if (reader_id === undefined || reader_id === null || reader_id == 0) {
		    // skip
		} else {
		    const reader_name = readerid2readers[reader_id];
		    if (reader_name === undefined || reader_name === null) {
			//data[i].reader_info = '-';
		    } else {
			//data[i].reader_name = reader_name;
			data[i].reader_info = reader_name + ', квиток №' + reader_id;
		    }
		}
	    }
	    this.instances = data;
	},
        instanceURL(id) {
	    return "/instances.html?id=" + id;
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

Vue.createApp(instancesApp).mount('#app');

