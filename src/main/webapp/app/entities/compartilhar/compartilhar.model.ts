import { User } from '../../shared';
export class Compartilhar {
    constructor(
        public id?: number,
        public tipo?: string,
        public mensagem?: string,
        public codigo?: string,
        public nome?: string,
        public status?: number,
        public remetente?: User,
        public destinatario?: User,
    ) {
    }
}
