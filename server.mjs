// server.mjs
import { createServer } from 'node:http';
import { readFile } from 'node:fs/promises';
import { extname, join } from 'node:path';
import { existsSync } from 'node:fs';

// MIME típusok hozzárendelése kiterjesztés alapján
const mimeTypes = {
  '.html': 'text/html',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.png': 'image/png',
  '.gif': 'image/gif',
  '.css': 'text/css',
  '.js': 'application/javascript',
  '.ico': 'image/x-icon'
};

const server = createServer(async (req, res) => {
  // Biztonságos alapkönyvtár
  const basePath = process.cwd(); // aktuális mappa
  let filePath = req.url === '/' ? '/index.html' : req.url;

  const fullPath = join(basePath, decodeURIComponent(filePath));
  
  // Ellenőrizzük, létezik-e a fájl
  if (existsSync(fullPath)) {
    try {
      const data = await readFile(fullPath);
      const ext = extname(fullPath).toLowerCase();
      const contentType = mimeTypes[ext] || 'application/octet-stream';
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(data);
    } catch (err) {
      res.writeHead(500, { 'Content-Type': 'text/plain' });
      res.end('Hiba a fájl olvasásakor');
    }
  } else {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('404 Nem található');
  }
});

server.listen(3000, '127.0.0.1', () => {
  console.log('Szerver fut: http://127.0.0.1:3000');
});

// run with `node server.mjs`