import { Projeto } from '../projeto';
export class Base {
    constructor(
        public id?: number,
        public nome?: string,
        public local?: string,
        public projeto?: Projeto,
    ) {
    }
}
