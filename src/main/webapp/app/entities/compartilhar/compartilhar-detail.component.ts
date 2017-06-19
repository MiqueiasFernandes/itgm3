import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Compartilhar } from './compartilhar.model';
import { CompartilharService } from './compartilhar.service';

@Component({
    selector: 'jhi-compartilhar-detail',
    templateUrl: './compartilhar-detail.component.html'
})
export class CompartilharDetailComponent implements OnInit, OnDestroy {

    compartilhar: Compartilhar;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private compartilharService: CompartilharService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['compartilhar']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCompartilhars();
    }

    load(id) {
        this.compartilharService.find(id).subscribe((compartilhar) => {
            this.compartilhar = compartilhar;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCompartilhars() {
        this.eventSubscriber = this.eventManager.subscribe('compartilharListModification', (response) => this.load(this.compartilhar.id));
    }
}
