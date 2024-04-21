FROM node:16.16.0

WORKDIR /frontend

COPY ./package*.json ./

RUN npm install

COPY . .

CMD ["npm", "start"]
