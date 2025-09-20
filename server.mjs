// server.mjs
import { createServer } from 'node:http';
import { readFile } from 'node:fs/promises';
import { extname, join } from 'node:path';
import { existsSync } from 'node:fs';
import db from './db.mjs';

// MIME típusok hozzárendelése
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

const parseJsonBody = async (req) => {
  return new Promise((resolve, reject) => {
    let body = '';
    req.on('data', chunk => body += chunk);
    req.on('end', () => {
      try {
        resolve(JSON.parse(body));
      } catch (e) {
        reject(e);
      }
    });
  });
};

const server = createServer(async (req, res) => {
  const method = req.method;
  const url = req.url;

  // API végpontok
  const routes = [
    'users', 'etelek', 'italok', 'menuk', 'rendelesek'
  ];

  for (const route of routes) {
    if (url === `/${route}` && method === 'GET') {
      const rows = db.prepare(`SELECT * FROM ${route}`).all();
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify(rows));
      return;
    }

    if (url === `/${route}` && method === 'POST') {
      try {
        const data = await parseJsonBody(req);

        // egyszerűsített beszúrás: kulcsok és értékek automatikus kigyűjtése
        const keys = Object.keys(data);
        const placeholders = keys.map(() => '?').join(', ');
        const stmt = db.prepare(
          `INSERT INTO ${route} (${keys.join(',')}) VALUES (${placeholders})`
        );
        stmt.run(...keys.map(k => data[k]));

        res.writeHead(201, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ message: `${route} hozzáadva` }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: err.message }));
      }
      return;
    }
  }

  // Statikus fájl kiszolgálás
  const basePath = process.cwd();
  let filePath = req.url === '/' ? '/index.html' : req.url;
  const fullPath = join(basePath, decodeURIComponent(filePath));

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