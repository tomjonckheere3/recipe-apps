const jwt = require('jsonwebtoken');
const dotenv = require('dotenv');

dotenv.config();
process.env.config;

function generateAccessToken(userData) {
    return jwt.sign(userData, process.env.TOKEN_SECRET);
}

function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];
  
    console.log(authHeader)

    if (token == null) {
        return res.sendStatus(401)
    }
  
    jwt.verify(token, process.env.TOKEN_SECRET, (err, user) => {
  
      if (err) {
            console.log(err)
            return res.sendStatus(403);
      }

      req.tokenUserId = user.userId;

      next();
    })
}

module.exports = {
    generateAccessToken,
    authenticateToken
}