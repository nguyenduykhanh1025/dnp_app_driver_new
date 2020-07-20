const path = require('path');
const fs = require('fs');
//joining path of directory
const directoryPath = path.join(__dirname, './src/assets/icons');
const indexFilePath = directoryPath + '/index.js';
//passsing directoryPath and callback function
fs.readdir(directoryPath, function(err, files) {
  //handling error
  if (err) {
    return console.log('Unable to scan directory: ' + err);
  }

  fs.writeFile(indexFilePath, ' \n', function() {});

  files
    .filter(f => {
      const ext = f
        .split('.')
        .pop()
        .toLocaleLowerCase();
      return ext === 'png' || ext === 'jpg';
    })
    .forEach(function(file) {
      const data = `export const ${file
        .split('.')[0]
        .replace(' ', '')
        .replace('-', '')} = require('@/assets/icons/${file}'); \n`;
      fs.appendFile(indexFilePath, data, function(err) {
        if (err) throw err;
      });
    });

  console.log(
    '\x1b[34m Icons were generated. Please see in /assets/icons/index.js',
  );
});
