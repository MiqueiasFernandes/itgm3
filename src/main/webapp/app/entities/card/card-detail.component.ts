import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Card } from './card.model';
import { CardService } from './card.service';

@Component({
    selector: 'jhi-card-detail',
    templateUrl: './card-detail.component.html'
})
export class CardDetailComponent implements OnInit, OnDestroy {

    card: Card;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private cardService: CardService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['card']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCards();
    }

    load(id) {
        this.cardService.find(id).subscribe((card) => {
            this.card = card;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCards() {
        this.eventSubscriber = this.eventManager.subscribe('cardListModification', (response) => this.load(this.card.id));
    }
}
