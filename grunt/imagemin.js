module.exports = {
    all: {
        files: [{
            expand: true,
            cwd: 'src',
            src: ['../src/main/webapp/images/*.{png,jpg,gif}'],
            dest: '../dist/'
        }]
    }
};