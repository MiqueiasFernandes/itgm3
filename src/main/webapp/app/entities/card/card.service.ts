import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Card } from './card.model';
import {Cenario} from "../cenario/cenario.model";
@Injectable()
export class CardService {

    private resourceUrl = 'api/cards';
    private resourceSearchUrl = 'api/_search/cards';

    constructor(private http: Http) { }

    create(card: Card): Observable<Card> {
        const copy: Card = Object.assign({}, card);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(card: Card): Observable<Card> {
        const copy: Card = Object.assign({}, card);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Card> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            ;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }

    getCards(): Observable<Card[]> {
        return this.query({
            page: 0,
            size: 777,
            sort: ['id']
        }).map((res: Response ) => res.json());
    }

    getCardsByCenario(cenario: Cenario): Observable<Card[]> {
        return this.getCards()
            .map((cards: Card[]) => {

                let cars: Card[] = [];

                cards.forEach((card: Card) => {
                    if(card.cenario.id === cenario.id) {
                        cars.push(card);
                    }
                });

                return cars;

            });
    }
}
