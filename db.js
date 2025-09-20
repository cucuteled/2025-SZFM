// db.mjs
import Database from 'better-sqlite3';
import { join, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';
import { existsSync, mkdirSync } from 'node:fs';

// __dirname replacement for ESM
const __dirname = dirname(fileURLToPath(import.meta.url));

// Biztosítsuk, hogy a /data könyvtár létezik
const dataDir = join(__dirname, 'data');
if (!existsSync(dataDir)) {
  mkdirSync(dataDir);
}

// Adatbázis fájl elérési útja
const dbPath = join(dataDir, 'mydb.sqlite');
const db = new Database(dbPath);

// Táblák létrehozása, ha nem léteznek
// user tábla (id, név, email, létrehozásidőpontka)
db.prepare(`
  CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nev TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    letrehozva TEXT NOT NULL
  )
`).run();

// Étel tábla (id ,étel neve, ára )
db.prepare(`
  CREATE TABLE IF NOT EXISTS etelek (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nev TEXT NOT NULL,
    ar INTEGER NOT NULL CHECK (ar > 0)
  )
`).run();

// Ital tábla (id ,ital neve, ára )
db.prepare(`
  CREATE TABLE IF NOT EXISTS italok (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nev TEXT NOT NULL,
    ar INTEGER NOT NULL CHECK (ar > 0)
  )
`).run();

// menü tábla (id , menü neve, tartalmazott étel idja pl(1 és 2 étel: 1;2), tartalmazott ital idja (szintúgy pl 21es ital és 9es ital és 3as ital : 21;9;3), ára )
db.prepare(`
  CREATE TABLE IF NOT EXISTS etelek (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nev TEXT NOT NULL,
    foods TEXT NOT NULL,
    drinks TEXT NOT NULL,
    ar INTEGER NOT NULL CHECK (ar > 0)
  )
`).run();

// Rendelések tábla (id , felhasználó neve amit leadott rendelés, rendelés étel tartalma, rendelés ital tartalma, rendelés ára )
db.prepare(`
  CREATE TABLE IF NOT EXISTS etelek (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usernev TEXT NOT NULL,
    orderedfoods TEXT NOT NULL,
    ordereddrinks TEXT NOT NULL,
    fizetettar INTEGER NOT NULL CHECK (ar > 0)
  )
`).run();


// Exportáljuk az adatbázist
export default db;
