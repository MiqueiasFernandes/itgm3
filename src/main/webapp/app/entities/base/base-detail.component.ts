import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Base } from './base.model';
import { BaseService } from './base.service';

@Component({
    selector: 'jhi-base-detail',
    templateUrl: './base-detail.component.html'
})
export class BaseDetailComponent implements OnInit, OnDestroy {

    base: Base;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private baseService: BaseService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['base']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBases();
    }

    load(id) {
        this.baseService.find(id).subscribe((base) => {
            this.base = base;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBases() {
        this.eventSubscriber = this.eventManager.subscribe('baseListModification', (response) => this.load(this.base.id));
    }
}
