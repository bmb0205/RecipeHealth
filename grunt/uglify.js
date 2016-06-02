module.exports = {
    all: {
        files: [{
            expand: true,
            cwd: 'src',
            src: '../src/main/webapp/js/*.js',
            dest: 'dist/scripts',
            ext: '.min.js'
        }]
    }
};
